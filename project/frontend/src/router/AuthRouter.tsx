import { Redirect, Route, useLocation } from 'react-router-dom';
import Constant from '../utils/Constant';
import Layout from "../components/Layout";


const AuthLayout = ({ children, ...rest }) => {
    return (
        <Layout>
            {children}
        </Layout>

    )
}



const AuthRouter = ({ component: Component, ...rest }) => {
    const data = JSON.parse(localStorage.getItem(Constant.KEY.USER) as any);



    return (
        data?.typeOfUser
            ? (
                data.typeOfUser === 'root'
                    ? (
                        <Redirect exact to={{ pathname: "/admin" }} />
                    )


                    :
                    <Redirect exact to={{ pathname: "/home" }} />
            )

            :
            <Route {...rest} render={matchProps => (
                <AuthLayout>
                    <Component {...matchProps} />
                </AuthLayout>
            )} />
    )
};

export default AuthRouter;