package hiber.dao;

import hiber.model.User;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.TypedQuery;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      sessionFactory.getCurrentSession().save(user);
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User u join fetch u.car");
      return query.getResultList();
   }

   @Transactional
   @Override
   public Optional<User> getUserByCar(String model, int series) {
      try {
         Session session = sessionFactory.getCurrentSession();
         TypedQuery<User> query = session.createQuery(
                 "FROM User u LEFT JOIN FETCH u.car car WHERE car.model = :model AND car.series = :series", User.class);
         query.setParameter("model", model);
         query.setParameter("series", series);
         return Optional.of(query.getSingleResult());
      } catch (NullPointerException | NoResultException e) {
         System.out.println("Пользователь с моделью " + model + " и серией " + series + " не найден");
         return Optional.empty();
      }
   }


}
