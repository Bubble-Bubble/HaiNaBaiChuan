package io.xujiaji.hnbc.presenters;

import cn.bmob.v3.exception.BmobException;
import io.xujiaji.hnbc.activities.MainActivity;
import io.xujiaji.hnbc.config.C;
import io.xujiaji.hnbc.contracts.LoginContract;
import io.xujiaji.hnbc.factory.ErrMsgFactory;
import io.xujiaji.hnbc.fragments.BaseMainFragment;
import io.xujiaji.hnbc.model.check.LoginCheck;
import io.xujiaji.hnbc.model.entity.User;
import io.xujiaji.hnbc.model.net.NetRequest;
import io.xujiaji.hnbc.utils.MD5Util;
import io.xujiaji.hnbc.utils.ToastUtil;

/**
 * Created by jiana on 16-11-4.
 */

public class LoginPresenter extends BasePresenter implements LoginContract.Presenter {
    private LoginContract.View view;

    public LoginPresenter(LoginContract.View view) {
        super(view);
        this.view = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void end() {
        view = null;
    }

    @Override
    public void requestLogin(String name, String password) {
        String nameErr = LoginCheck.checkAccount(name);
        String passwordErr = LoginCheck.checkPassword(password);
        if (nameErr != null) {
            view.nameFormatError(nameErr);
            return;
        }

        if (passwordErr != null) {
            view.passwordFormatError(passwordErr);
            return;
        }

        NetRequest.Instance().login(name, MD5Util.getMD5(password), new NetRequest.RequestListener<User>() {
            @Override
            public void success(User user) {
                view.callLoginSuccess();
            }

            @Override
            public void error(BmobException err) {
                view.callLoginFail(ErrMsgFactory.errMSG(err.getErrorCode()));
            }
        });
    }

    @Override
    public void requestSina() {
        ToastUtil.getInstance().showShortT("未知区域：新浪登录");
    }

    @Override
    public void requestQQ() {
        ToastUtil.getInstance().showShortT("未知区域：QQ登录");
    }

    @Override
    public void requestWeiXin() {
        ToastUtil.getInstance().showShortT("未知区域：微信登录");
    }

    @Override
    public void requestRegistered(final BaseMainFragment loginFragment) {
        final MainActivity mainActivity = (MainActivity) loginFragment.getActivity();
        mainActivity.goToFragment(C.fragment.REGISTER);
    }

}
