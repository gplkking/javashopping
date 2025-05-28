import db.DBUtil;
import view.HomeFrame;

public class Main {
    public static void main(String[] args) {
        DBUtil.init();  // 드라이버 등록
        new HomeFrame().setVisible(true);  // 첫 화면 띄우기
    }
}
