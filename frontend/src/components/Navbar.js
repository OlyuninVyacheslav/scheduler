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
      <nav className="h-20 mb-12 bg-gray-800">
        <div className="mx-auto text-white text-lg font-semibold max-w-screen-xl">
          <ul className="flex">
            <li className="flex items-center mr-8">
              <img
                className="h-20 p-2"
                src={require("../pictures/logo.png")}
                alt="Logo"
              />
              <p className="text-white text-3xl">scheduler</p>
            </li>
            {isAuthenticated ? (
              <>
                <li className="hover:bg-gray-700 flex items-cetner px-2">
                  <NavLink to="/boards" className="h-full flex items-center">
                    Доски
                  </NavLink>
                </li>
                <li className="hover:bg-gray-700 flex items-center px-2 ml-auto">
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
               <li className="hover:bg-gray-700 flex items-cetner px-2">
                  <NavLink to="/test" className="h-full flex items-center">
                    /test
                  </NavLink>
                </li>
                <li className="hover:bg-gray-700 flex items-cetner px-2">
                  <NavLink to="/boards" className="h-full flex items-center">
                    /boards
                  </NavLink>
                </li>
              <li className="hover:bg-gray-700 flex items-cetner px-2 ml-auto">
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
