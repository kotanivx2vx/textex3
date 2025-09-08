package textex3;

/**
 * メニュー選択肢を意味構造で表現する列挙型。
 * 数値と意味を対応させ、switch文の可読性と安全性を向上。
 * UI層における魔法数字の排除と、意味の明示化を目的とする。
 */
public enum MenuOption {
    INBOUND(1),   // 入庫登録
    PREPARE(2),   // 出庫準備
    OUTBOUND(3),  // 出庫実行
    SHOW(4),      // 在庫一覧表示
    EXIT(5);      // アプリ終了

    private final int code;

    /**
     * 数値コードと意味を結びつけるコンストラクタ。
     * @param code ユーザー入力に対応する数値
     */
    MenuOption(int code) {
        this.code = code;
    }

    /**
     * 数値コードを取得。
     * @return メニュー選択肢の数値
     */
    public int getCode() {
        return code;
    }

    /**
     * 数値から対応する MenuOption を取得する。
     * 該当しない場合は null を返すことで、UI層で無効入力を判定可能。
     * @param code ユーザー入力の数値
     * @return 対応する MenuOption、または null
     */
    public static MenuOption fromCode(int code) {
        for (MenuOption option : values()) {
            if (option.code == code) return option;
        }
        return null;
    }
}
