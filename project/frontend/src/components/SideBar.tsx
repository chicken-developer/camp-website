import React from "react";
import { Link } from "react-router-dom"
import styled from "styled-components";
import './sidebar.css'
// let SidebarWrapper = styled.div`

//     #sidebar-wrapper{
        
//         .sidebar-menu{
//             li{ 
//                 background-color: 'red'
//             }   
//         }
//     }
// `

export const Sidebar = () => {
    return (
        <div className="main-sidebar">
            <aside id="sidebar-wrapper">
                <div className="sidebar-brand">
                    <a href="#">Stisla</a>
                </div>
                <ul className="sidebar-menu">
                    <li className="active">
                        <Link className="nav-link" to="/admin/users">
                            <i className="far fa-square"></i>
                            <span>Users</span>
                        </Link>
                    </li>
                    <li>
                        <Link className="nav-link" to="/admin/camps">
                            <i className="fas fa-pencil-ruler"></i>
                            <span>Camps</span>
                        </Link>
                    </li>
                </ul>
            </aside>
        </div>
    )
}

export default Sidebar;