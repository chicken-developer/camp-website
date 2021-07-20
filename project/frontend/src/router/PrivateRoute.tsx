import { Redirect, Route } from "react-router-dom";

function PrivateRoute({ component: Component, authentication, ...rest }: any) {
  return (
    <Route
      {...rest}
      render={(props) =>
        authentication === true ? (
          <Component {...props} />
        ) : (
          <Redirect
            to={{ pathname: "/home", state: { from: props.location } }}
          />
        )
      }
    />
  );
}
export default PrivateRoute;
