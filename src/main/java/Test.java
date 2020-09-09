import Entity.User;
import Mapper.userMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;

public class Test {
    public static void main(String args[]) throws IOException {

        String resource = "mybatis-config.xml"; //默认配置文件从resource下读的
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        /*
        String queryStr = "sgj/src/main/resources/userMapper.getUser";//映射sql的标识字符串
        //执行查询返回一个唯一user对象的sql
        User user = session.selectOne(queryStr, 4);

         */
        userMapper mapper = session.getMapper(userMapper.class);
        User u = mapper.getUser(1);
        System.out.println(u.getName());

    }
    }


