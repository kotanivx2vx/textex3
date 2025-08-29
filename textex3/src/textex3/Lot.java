import java.time.LocalDateTime;

/**
 * 商品のロット情報を表すクラス。
 * 各ロットは数量と入庫日時を持ち、倉庫や出庫準備の管理単位となる。
 */
public class Lot {
    // ロット番号（一意に識別される）
    private long lotNumber;

    // ロット内の商品の数量
    private int quantity;

    // 入庫日時（ロットが倉庫に登録された時刻）
    private LocalDateTime receivedAt;

    /**
     * コンストラクタ：ロット番号と数量を指定してロットを生成。
     * 入庫日時は生成時点の現在時刻を自動的に設定する。
     *
     * @param lotNumber ロット番号（10桁想定）
     * @param quantity  数量（5桁まで想定）
     */
    public Lot(long lotNumber, int quantity) {
        this.lotNumber = lotNumber;
        this.quantity = quantity;
        this.receivedAt = LocalDateTime.now(); // 入庫時刻を現在時刻で初期化
    }

    /**
     * ロット番号を取得する。
     *
     * @return ロット番号
     */
    public long getLotNumber() {
        return lotNumber;
    }

    /**
     * 数量を取得する。
     *
     * @return 数量
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * 入庫日時を取得する。
     *
     * @return 入庫日時
     */
    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }
}

