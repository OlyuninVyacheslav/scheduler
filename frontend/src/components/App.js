import React, { useEffect, useState } from 'react';
import {Navbar} from "./Navbar";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { getAuthToken } from '../helpers/axios_helper'
import LoginForm from './LoginForm';

function App() {
  const [isAuthenticated] = useState(getAuthToken() !== null && getAuthToken() !== "null");
  return (
    <BrowserRouter>
      <Navbar  isAuthenticated={isAuthenticated}/>
      <Routes>
      {isAuthenticated ? (
              <>
              </>
            ) : (
              <>
                <Route path="/" element={<LoginForm />} />
              </>
            )}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
