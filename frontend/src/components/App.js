import React, { useEffect, useState } from 'react';
import {Navbar} from "./Navbar";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { getAuthToken } from '../helpers/axios_helper'
import LoginForm from './LoginForm';
import BoardsList from './BoardsList';
import Board from './main/Board'

function App() {
  const [isAuthenticated] = useState(getAuthToken() !== null && getAuthToken() !== "null");
  return (
    <BrowserRouter>
      <Navbar  isAuthenticated={isAuthenticated}/>
      <Routes>
      {isAuthenticated ? (
              <>
                <Route path="/boards" element={<BoardsList/>}/>
                <Route path="/boards/:boardId" element={<Board />} />
                <Route path="*" element={<h2>Ресурс не найден</h2>} />
              </>
            ) : (
              <>
                <Route path="/" element={<LoginForm />} />
                <Route path="/boards" element={<BoardsList/>}/>
                {/* <Route path="/test" element={<Board/>}/> */}
                <Route path="/boards/:boardId" element={<Board/>} />
              </>
            )}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
