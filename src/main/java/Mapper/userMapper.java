package Mapper;
import Entity.User;
import Entity.Node;

import java.util.Date;

public interface userMapper {
    User getUser(Integer id);
    int insertNode(Node n);
}
