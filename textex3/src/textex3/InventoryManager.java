package textex3;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 * 在庫管理の中心的なロジックを担うクラス。
 * 商品ごとのロットを倉庫（FIFO）と出庫準備（LIFO）で管理する。
 * 責務分離・構造化・抽象化を意識した設計。
 */
public class InventoryManager {

    // 倉庫内の商品ロットを管理するマップ（FIFO：LinkedListによるキュー）
    private Map<Long, Queue<Lot>> warehouse = new HashMap<>();

    // 出庫準備中の商品ロットを管理するマップ（LIFO：Stackによるスタック）
    private Map<Long, Stack<Lot>> outboundPrep = new HashMap<>();

    /**
     * 入庫処理：指定された商品IDに対してロット情報を倉庫に登録する。
     * 商品IDが未登録の場合は新たにキューを生成。
     *
     * @param productId 商品ID（10桁想定）
     * @param lotNumber ロット番号（10桁想定）
     * @param quantity  数量（5桁まで想定）
     */
    public void registerInbound(long productId, long lotNumber, int quantity) {
        warehouse.putIfAbsent(productId, new LinkedList<>());
        warehouse.get(productId).add(new Lot(lotNumber, quantity));
    }

    /**
     * 出庫準備処理：倉庫から最も古いロットを取り出し、出庫準備エリアに移動する。
     * FIFO → LIFO への責務移動を表現。
     *
     * @param productId 商品ID
     * @return 移動されたロット情報（存在しない場合は null）
     */
    public Lot prepareOutbound(long productId) {
        Queue<Lot> queue = warehouse.get(productId);
        if (queue == null || queue.isEmpty()) return null;

        Lot lot = queue.poll(); // 最古のロットを取り出す
        outboundPrep.putIfAbsent(productId, new Stack<>());
        outboundPrep.get(productId).push(lot); // 出庫準備エリアに積む（LIFO）
        return lot;
    }

    /**
     * 出庫処理：出庫準備エリアから最後に準備されたロットを取り出す。
     * LIFOに基づく出庫順を実現。
     *
     * @param productId 商品ID
     * @return 出庫されたロット情報（存在しない場合は null）
     */
    public Lot executeOutbound(long productId) {
        Stack<Lot> stack = outboundPrep.get(productId);
        if (stack == null || stack.isEmpty()) return null;

        return stack.pop(); // 最後に準備されたロットを出庫
    }

    /**
     * 在庫一覧表示処理：倉庫内と出庫準備中の在庫状況を整形して表示する。
     * 表示順や最古の入庫日時の取得も含め、構造化された出力を行う。
     */
    public void showInventory() {
        System.out.println("--- 倉庫内 ---");

        // 商品ID昇順で倉庫内の在庫を表示
        warehouse.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                Queue<Lot> queue = entry.getValue();
                if (!queue.isEmpty()) {
                    Lot oldest = queue.peek(); // 最古のロット（先頭）
                    System.out.printf("%d : %d : %s%n",
                        entry.getKey(), queue.size(), oldest.getReceivedAt());
                }
            });

        System.out.println("--- 出庫準備中 ---");

        // 在庫数降順で出庫準備中の在庫を表示
        outboundPrep.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue().size(), a.getValue().size()))
            .forEach(entry -> {
                Stack<Lot> stack = entry.getValue();
                if (!stack.isEmpty()) {
                    Lot oldest = stack.lastElement(); // 最古のロット（スタックの底）
                    System.out.printf("%d : %d : %s%n",
                        entry.getKey(), stack.size(), oldest.getReceivedAt());
                }
            });
    }
}
