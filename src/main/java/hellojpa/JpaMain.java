package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        //entityManagerFactory는 DB당 하나만 생성(애플리케이션 전체에서 공유)
        EntityManagerFactory entityManagerFactory=Persistence.createEntityManagerFactory("hello");
        //고객 요청 마다 생성되고 close(따라서 쓰레드간에 공유 절대 x, 사용하고 버려야 )
        EntityManager entityManager=entityManagerFactory.createEntityManager();

        EntityTransaction entityTransaction=entityManager.getTransaction();

        //JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
        entityTransaction.begin();
        try{
            /*
            Member findMember=entityManager.find(Member.class,1L);
            findMember.setName("HelloJPA");
            //persist로 저장할 필요 x, 자바 컬렉션을 다루듯이 다루도록 설계되었기 때문

            jpa를 통해서 엔티티를 가져오면 이는 jpa가 관리
            jpa가 변경여부를 트랜잭션 commit하는 시점에 check
            변경되면 update 쿼리를 만들어서 날림(트랜잭션 commit 직전에)

            entityTransaction.commit();
            */

            //JPQL, 객체 지향 쿼리
            List<Member> result=entityManager.createQuery("select m from Member as m",Member.class)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();
            for(Member member: result){
                System.out.println("member.name ="+member.getName());
            }
        }catch(Exception e){
            entityTransaction.rollback();
        }finally{
            entityManager.close();
        }

        //전체 애플리케이션이 완전히 끝나게 되면 entityManagerFactory를 닫아줌
        entityManagerFactory.close();
    }
}
