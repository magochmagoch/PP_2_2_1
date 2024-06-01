package hiber.service;

import hiber.dao.UserDao;
import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private UserDao userDao;

    @Transactional
    @Override
    public void add(User user) {
        userDao.add(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Transactional
    @Override
    public User getUserByCar(String model, int series) {
        try {
            Session session = sessionFactory.getCurrentSession();
            TypedQuery<User> query = session.createQuery(
                    "SELECT u FROM User u WHERE u.car.model = :model AND u.car.series = :series", User.class);
            query.setParameter("model", model);
            query.setParameter("series", series);
            return query.getSingleResult();
        } catch (NullPointerException | NoResultException e) {
            System.out.println("Пользователь с моделью " + model + " и серией " + series + " не найден");
        }
        return null;
    }

}
