package org.yuan.service.center;

import org.yuan.pojo.OrderItems;
import org.yuan.pojo.bo.conter.MyCommentBO;

import java.util.List;

public interface MyCommentService {

    List<OrderItems> queryPendingComment(String orderId);

    void saveCommnets(String orderId ,String userId,List<MyCommentBO> orderItemList);
}
