import React, { useState } from 'react';
import { request, setAuthHeader } from '../helpers/axios_helper';
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const LoginForm = () => {
  const [activeTab, setActiveTab] = useState('login');
  const [formData, setFormData] = useState({
    surname: '',
    firstname: '',
    patronymic: '',
    email: '',
    password: ''
  });

  const onChangeHandler = (event) => {
    const { name, value } = event.target;
    setFormData({ ...formData, [name]: value });
  };

  const onSubmitLogin = (e) => {
    e.preventDefault();
    request(
        "POST",
        "/login",
        {
            email: formData.email,
            password: formData.password
        }).then(
        (response) => {
            setAuthHeader(response.data.token);
            window.location.reload();
        }).catch(
        (error) => {
          setAuthHeader(null);
            toast.error('Неверный логин или пароль!', {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
        }
    );
  };

  const onSubmitRegister = (e) => {
    e.preventDefault();
    request(
        "POST",
        "/register",
        {
            surname: formData.surname,
            firstname: formData.firstname,
            patronymic: formData.patronymic,
            email: formData.email,
            password: formData.password
        }).then(
        (response) => {
            setAuthHeader(response.data.token);
            window.location.reload();
        }).catch(
        (error) => {
            setAuthHeader(null);
            toast.error('Произошла ошибка при регистрации!', {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
        }
    );
  };

  return (
    <div className="flex justify-center">
      <div className="w-72">
        <ul className="flex mb-3" role="tablist">
          <li className="flex-1">
            <button
              className={`w-full py-2 rounded-t-lg ${activeTab === 'login' ? 'bg-gray-500 text-white' : 'bg-gray-300'}`}
              onClick={() => setActiveTab('login')}
            >
              Вход
            </button>
          </li>
          <li className="flex-1">
            <button
              className={`w-full py-2 rounded-t-lg ${activeTab === 'register' ? 'bg-gray-500 text-white' : 'bg-gray-300'}`}
              onClick={() => setActiveTab('register')}
            >
              Регистрация
            </button>
          </li>
        </ul>

        <div>
          <div className={`p-4 ${activeTab === 'login' ? 'block' : 'hidden'}`}>
            <form onSubmit={onSubmitLogin}>
              <div className="mb-4">
                <input
                  type="email"
                  id="email"
                  name="email"
                  className="w-full p-2 border border-gray-300 rounded"
                  onChange={onChangeHandler}
                  placeholder="Эл. почта"
                />
              </div>
              <div className="mb-4">
                <input
                  type="password"
                  id="loginPassword"
                  name="password"
                  className="w-full p-2 border border-gray-300 rounded"
                  onChange={onChangeHandler}
                  placeholder="Пароль"
                />
              </div>
              <button type="submit" className="w-full py-2 bg-gray-500 text-white rounded">Войти</button>
            </form>
          </div>

          <div className={`p-4 ${activeTab === 'register' ? 'block' : 'hidden'}`}>
            <form onSubmit={onSubmitRegister}>
              <div className="mb-4">
                <input
                  type="text"
                  id="surname"
                  name="surname"
                  className="w-full p-2 border border-gray-300 rounded"
                  onChange={onChangeHandler}
                  placeholder="Фамилия"
                />
              </div>
              <div className="mb-4">
                <input
                  type="text"
                  id="firstname"
                  name="firstname"
                  className="w-full p-2 border border-gray-300 rounded"
                  onChange={onChangeHandler}
                  placeholder="Имя"
                />
              </div>
              <div className="mb-4">
                <input
                  type="text"
                  id="patronymic"
                  name="patronymic"
                  className="w-full p-2 border border-gray-300 rounded"
                  onChange={onChangeHandler}
                  placeholder="Отчество"
                />
              </div>
              <div className="mb-4">
                <input
                  type="email"
                  id="email"
                  name="email"
                  className="w-full p-2 border border-gray-300 rounded"
                  onChange={onChangeHandler}
                  placeholder="Эл. почта"
                />
              </div>
              <div className="mb-4">
                <input
                  type="password"
                  id="registerPassword"
                  name="password"
                  className="w-full p-2 border border-gray-300 rounded"
                  onChange={onChangeHandler}
                  placeholder="Пароль"
                />
              </div>
              <button type="submit" className="w-full py-2 bg-gray-500 text-white rounded">Зарегистрироваться</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginForm;
