package com.ginDriver.main.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * md5文件表
 *
 * @author DueGin
 * @TableName md5_file
 */
@TableName(value = "md5_file")
@Data
public class Md5File implements Serializable {

    @TableId
    private String md5;

    /**
     * 存放路径
     */
    private String src;

    /**
     * 对象存储名称
     */
    private String objectName;

    /**
     * 引用次数
     */
    private Integer ref;

    /**
     * 软删除
     */
    private Integer deleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Md5File other = (Md5File) that;
        return (this.getMd5() == null ? other.getMd5() == null : this.getMd5().equals(other.getMd5()))
                && (this.getSrc() == null ? other.getSrc() == null : this.getSrc().equals(other.getSrc()))
                && (this.getRef() == null ? other.getRef() == null : this.getRef().equals(other.getRef()))
                && (this.getDeleted() == null ? other.getDeleted() == null : this.getDeleted().equals(other.getDeleted()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMd5() == null) ? 0 : getMd5().hashCode());
        result = prime * result + ((getSrc() == null) ? 0 : getSrc().hashCode());
        result = prime * result + ((getRef() == null) ? 0 : getRef().hashCode());
        result = prime * result + ((getDeleted() == null) ? 0 : getDeleted().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", md5=").append(md5);
        sb.append(", src=").append(src);
        sb.append(", ref=").append(ref);
        sb.append(", deleted=").append(deleted);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}