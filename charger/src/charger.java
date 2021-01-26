import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class charger {

    public static void main(String[] args){
        Scanner  scan= new Scanner(System.in);
        int order=scan.nextInt();
        if(order==1){

            String name=scan.next();
            String author=scan.next();
            charger.addBook(name, author);
        }
        if ((order==2)){

            String name=scan.next();
            charger.lendBook(name);
        }
        System.out.println("执行语句成功");
    }


    /**
     * 取得数据库的连接
     * @return 一个数据库的连接
     */
    public static Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/library?characterEncoding=UTF-8","root", "a99596537");
            //该类就在 mysql-connector-java-5.0.8-bin.jar中,如果忘记了第一个步骤的导包，就会抛出ClassNotFoundException
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 添加课程
     */
    public static void addBook(String bookName,String author){
        String sql = "insert into tbl(name,author,lended) values(?,?,?)";
        //该语句为每个 IN 参数保留一个问号（“？”）作为占位符
        Connection conn = null;				//和数据库取得连接
        PreparedStatement pstmt = null;		//创建statement
        try{
            conn = charger.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookName ); //给占位符赋值
            pstmt.setString(2, author  ); //给占位符赋值
            pstmt.setString(3, "NO" ); //给占位符赋值
            pstmt.executeUpdate();			//执行
        }catch(SQLException e){
            e.printStackTrace();
        }
        finally{
            charger.close(pstmt);
            charger.close(conn);		//必须关闭
        }
    }

    /**
     * 删除课程
     */
    public static void delBook(String bookName){
        String sql = "delete from tbl where name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = charger.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            charger.close(pstmt);
            charger.close(conn);		//必须关闭
        }
    }

    /**
     * 修改课程
     */
    public static void lendBook(String bookName){
        String sql = "update t_course set lended =? where name=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = charger.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "YES");
            pstmt.setString(2, bookName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            charger.close(pstmt);
            charger.close(conn);		//必须关闭
        }
    }

    /**
     * 封装三个关闭方法
     */
    public static void close(PreparedStatement pstmt){
        if(pstmt != null){						//避免出现空指针异常
            try{
                pstmt.close();
            }catch(SQLException e){
                e.printStackTrace();
            }

        }
    }

    public static void close(Connection conn){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    public static void close(ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }

    /**
     * 查询课程
     * @return
     */
    public static List<book> findBookList(){
        String sql = "select * from t_course order by course_id";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        //创建一个集合对象用来存放查询到的数据
        List<book> bookList = new ArrayList<>();
        try {
            conn = charger.getConnection();
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            rs = (ResultSet) pstmt.executeQuery();
            while (rs.next()){
                String Author = rs.getString("author");
                String Name = rs.getString("name");
                String lended = rs.getString("lended");
                //每个记录对应一个对象
                book book = new book();
                book.setName(Name);
                book.setAuthor(Author);
                book.setLended(lended);
                //将对象放到集合中
                bookList.add(book);
            }
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }finally{
            charger.close(pstmt);
            charger.close(conn);		//必须关闭
        }
        return bookList;
    }

}