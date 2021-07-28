import React from "react";
import {Link} from "react-router-dom"
import './sidebar.css'
export default function({}) {
    return (
        <div class="main-sidebar">
            <aside id="sidebar-wrapper">
                <div class="sidebar-brand">
                    <a href="#">Stisla</a>
                </div>
                <ul class="sidebar-menu">                
                    <li class="active">
                        <Link className="nav-link" to = "/admin/users">
                            <i class="far fa-square"></i> 
                            <span>Users</span>
                        </Link>
                    </li>
                    <li>
                        <Link class="nav-link" to = "/admin/camps">
                            <i class="fas fa-pencil-ruler"></i> 
                            <span>Camps</span>
                        </Link>
                    </li>
                </ul>
            </aside>
        </div>
    )
}