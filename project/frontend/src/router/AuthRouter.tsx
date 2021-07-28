import { Redirect, Route, useLocation } from 'react-router-dom';
import Constant from '../utils/Constant';
import Layout from "../components/Layout";
import { useSelector } from 'react-redux';
import { RootState } from '../store';
import * as Model from '../type/index'

const AuthLayout = ({ children, ...rest }) => {
    return (
        <Layout>
            {children}
        </Layout>

    )
}



const AuthRouter = ({ component: Component, ...rest }) => {
    const data = JSON.parse(localStorage.getItem(Constant.KEY.USER) as any);
    const authUser = useSelector((state: RootState) => state.auth.userData) as Model.User;

    return (
        authUser.username
            ? (
                data?.typeOfUser === 'root'
                    ? (
                        <Redirect exact to={{ pathname: "/admin" }} />
                    )
                    :
                    <Redirect exact to={{ pathname: "/home" }} />
            )
            : (
                <Route {...rest} render={matchProps => (
                    <AuthLayout>
                        <Component {...matchProps} />
                    </AuthLayout>
                )} />
            )
    )
};

export default AuthRouter;