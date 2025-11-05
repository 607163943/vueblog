package top.hcode.blog.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyBatisPlusMateObjectHandler implements MetaObjectHandler {
    /**
     * 新增数据填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始新增填充...");
        this.setInsertFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setInsertFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    /**
     * 修改数据填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始修改填充...");
        this.setUpdateFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
