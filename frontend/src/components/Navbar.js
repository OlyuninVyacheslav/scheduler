import React from "react";
import { NavLink } from "react-router-dom";
import { setAuthHeader } from "../helpers/axios_helper";

function logout() {
  setAuthHeader(null);
  window.location.reload();
  window.location.assign("/");
}

export const Navbar = ({ isAuthenticated }) => {
  return (
    <>
      <nav className="h-20 mb-12 bg-emerald-600">
        <div className="max-w-screen-xl mx-auto text-white text-lg font-semibold">
          <ul className="flex">
            <li>
              <img
                className="h-20 p-2 mr-8"
                src={require("../pictures/logo.png")}
                alt="Logo"
              />
            </li>
            {isAuthenticated ? (
              <>
                <li className="hover:bg-emerald-700 flex items-cetner px-2">
                  <NavLink to="/" className="h-full flex items-center">
                    Доски
                  </NavLink>
                </li>
                <li className="bg-emerald-700 hover:bg-emerald-800 flex items-center px-2 ml-auto">
                  <button
                    onClick={() => {
                      logout();
                    }}
                    className="h-full flex items-center"
                  >
                    Выйти
                  </button>
                </li>
              </>
            ) : (
              <>
              <li className="hover:bg-emerald-700 flex items-cetner px-2 ml-auto">
                  <NavLink to="/" className="h-full flex items-center">
                    Войти
                  </NavLink>
              </li>
              </>
            )}
          </ul>
        </div>
      </nav>
    </>
  );
};
