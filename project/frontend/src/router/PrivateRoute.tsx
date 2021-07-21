import { useSelector } from "react-redux";
import { Redirect, Route, useLocation } from "react-router-dom";

export const PrivateRoute = ({ component: Component, ...rest }: any) => {
  const auth = useSelector((state: any) => state.auth);
  const location = useLocation();
  return (
    <Route
      {...rest}
      render={(props) =>
        !auth?.isLogin ? (
          <Component {...props} />
        ) : (
          <Redirect to={{ pathname: "/home", state: { from: location } }} />
        )
      }
    />
  );
};
