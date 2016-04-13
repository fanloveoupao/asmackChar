package bruse.com.wechar.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

/**
 * Created by bruse on 16/3/31.
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userInfosDaoConfig;

    private final UserInfosDao userInfosDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userInfosDaoConfig = daoConfigMap.get(UserInfosDao.class).clone();
        userInfosDaoConfig.initIdentityScope(type);

        userInfosDao = new UserInfosDao(userInfosDaoConfig, this);

        registerDao(UserInfos.class, userInfosDao);
    }

    public void clear() {
        userInfosDaoConfig.getIdentityScope().clear();
    }

    public UserInfosDao getUserInfosDao() {
        return userInfosDao;
    }

}

