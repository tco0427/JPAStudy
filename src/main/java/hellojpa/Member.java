package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

//어노테이션을 통해 필요한 매핑 진행

@Entity
public class Member {

    @Id
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
