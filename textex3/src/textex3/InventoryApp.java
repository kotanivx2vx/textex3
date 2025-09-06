package textex3;

import java.util.Scanner;

/**
 * コンソールベースの在庫管理アプリケーションのエントリポイント。
 * ユーザーとの対話を通じて、InventoryManager に処理を委譲する。
 * UI層としての責務に徹し、ロジック層との分離を意識した設計。
 */
public class InventoryApp {

    // 在庫管理ロジックを担うマネージャー（ドメイン層）
    private static InventoryManager manager = new InventoryManager();

    // ユーザー入力を受け付けるスキャナ（UI層）
    private static Scanner scanner = new Scanner(System.in);

    /**
     * アプリケーションのメインループ。
     * メニュー表示 → ユーザー入力 → 処理分岐 → 繰り返し。
     * 想定外の例外発生時にはメッセージを表示して終了。
     */
    public static void main(String[] args) {
        try {
            while (true) {
                showMenu(); // メニュー表示
                int choice = getIntInput("選択 > "); // ユーザー選択
                switch (choice) {
                    case 1 -> handleInbound();     // 入庫登録
                    case 2 -> handlePrep();        // 出庫準備
                    case 3 -> handleOutbound();    // 出庫実行
                    case 4 -> manager.showInventory(); // 在庫一覧表示
                    case 5 -> {
                        System.out.println("終了します。");
                        return;
                    }
                    default -> System.out.println("無効な選択です。");
                }
            }
        } catch (Exception e) {
            System.out.println("予期せぬエラーが発生しました: " + e.getMessage());
        }
    }

    /**
     * メニュー画面の表示処理。
     * ユーザーに選択肢を提示する。
     */
    private static void showMenu() {
        System.out.println("\n--- 在庫管理メニュー ---");
        System.out.println("1. 入庫登録");
        System.out.println("2. 出庫準備登録");
        System.out.println("3. 出庫登録");
        System.out.println("4. 在庫一覧表示");
        System.out.println("5. 終了");
    }

    /**
     * 入庫登録処理。
     * ユーザーから商品ID・ロット番号・数量を受け取り、InventoryManager に登録を依頼。
     */
    private static void handleInbound() {
        long productId = getLongInput("商品ID > ");
        long lotNumber = getLongInput("ロット番号 > ");
        int quantity = getIntInput("数量 > ");
        manager.registerInbound(productId, lotNumber, quantity);
    }

    /**
     * 出庫準備処理。
     * ユーザーから商品IDを受け取り、最古のロットを準備エリアに移動。
     * 結果を画面に表示。
     */
    private static void handlePrep() {
        long productId = getLongInput("商品ID > ");
        Lot lot = manager.prepareOutbound(productId);
        if (lot != null) {
            System.out.printf("準備完了: ロット %d, 数量 %d, 入庫日時 %s%n",
                lot.getLotNumber(), lot.getQuantity(), lot.getReceivedAt());
        } else {
            System.out.println("対象商品が存在しません。");
        }
    }

    /**
     * 出庫処理。
     * ユーザーから商品IDを受け取り、最後に準備されたロットを出庫。
     * 結果を画面に表示。
     */
    private static void handleOutbound() {
        long productId = getLongInput("商品ID > ");
        Lot lot = manager.executeOutbound(productId);
        if (lot != null) {
            System.out.printf("出庫完了: ロット %d, 数量 %d, 入庫日時 %s%n",
                lot.getLotNumber(), lot.getQuantity(), lot.getReceivedAt());
        } else {
            System.out.println("出庫準備中の商品が存在しません。");
        }
    }

    /**
     * 整数入力を受け付けるユーティリティ。
     * 入力値が不正な場合は再入力を促す。
     *
     * @param prompt 表示するプロンプト文字列
     * @return 整数値
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("数値を入力してください。");
            }
        }
    }

    /**
     * 長整数入力を受け付けるユーティリティ。
     * 入力値が不正な場合は再入力を促す。
     *
     * @param prompt 表示するプロンプト文字列
     * @return 長整数値
     */
    private static long getLongInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("数値を入力してください。");
            }
        }
    }
}
