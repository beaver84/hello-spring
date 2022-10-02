package hello.hellospring.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "tb_user")
public class TbUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //DB에서 ID컬럼에 AI체크
    private Long no;

    @Column(name = "f_user_id")
    private String fUserId;

    @Column(name = "f_user_pwd")
    private String fUserPwd;

    @Column(name = "f_user_name")
    private String fUserName;
}
