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

            /*
            //JPQL, 객체 지향 쿼리
            List<Member> result=entityManager.createQuery("select m from Member as m",Member.class)
                    .setFirstResult(5)
                    .setMaxResults(8)
                    .getResultList();
            for(Member member: result){
                System.out.println("member.name ="+member.getName());
            }
             */

            /*
            //find로 조회를 하는데 DB에 select쿼리가 날라가지않음
            //persist로 저장 시 1차캐시에 저장하고, find에서 동일 PK로 조회했기 때문에 DB가 아닌 1차 캐시에서 우선적으로 조회

            Member member=new Member();
            member.setId(101L);
            member.setName("HelloJPA");
            entityManager.persist(member);
            Member findMember=entityManager.find(Member.class,101L);
            System.out.println("findMember.id= "+findMember.getId());
            System.out.println("findMember.name ="+findMember.getName());
            */

            /*
            //첫번째 Member객체만 select쿼리를 보내서 DB에서 가져옴.(영속 컨텍스트에 올림)
            //두번째 find부터는 영속 컨텍스트 안에 있는 1차 캐시에서 꺼내옴.

            Member findMember1=entityManager.find(Member.class,101L);
            Member findMember2=entityManager.find(Member.class,101L);

            System.out.println(findMember2==findMember1); //true
            */

            /*
            Member member1=new Member(150L,"A");
            Member member2=new Member(160L,"B");

            entityManager.persist(member1);
            entityManager.persist(member2);

            //commit하는 시점에 쿼리가 나감(버퍼링)
            System.out.println("===========================");
             */

            /*
            //엔티티 수정(변경 감지) -컬렉션 다루듯이-
            Member member=entityManager.find(Member.class,150L);
            member.setName("JJ");
             */

            /*
            Member member=new Member(200L,"member200");
            entityManager.persist(member);

            //flush()메소드를 직접 호출하여 영속성 컨텍스트를 강제로 플러시한다.
            entityManager.flush();

            System.out.println("=======================");
            */

            /*
            Member member=entityManager.find(Member.class,150L);
            member.setName("AAAAA");
            entityManager.detach(member);

            //commit시 아무일도 일어나지 않음(find에서 select쿼리만 나가고 setName을 통한 update쿼리 발생 x)
            entityTransaction.commit();
             */


            Member member=entityManager.find(Member.class,150L);
            member.setName("AAAAA");
            //1차 캐시 지워짐
            //테스트 케이스 작성 시 clear()가 도움이 됨
            entityManager.clear();
            //다시
            Member member2=entityManager.find(Member.class,150L);


            entityTransaction.commit();



        }catch(Exception e){
            entityTransaction.rollback();
        }finally{
            entityManager.close();
        }

        //전체 애플리케이션이 완전히 끝나게 되면 entityManagerFactory를 닫아줌
        entityManagerFactory.close();
    }
}
