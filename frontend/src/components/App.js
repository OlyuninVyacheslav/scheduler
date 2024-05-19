import React, { useEffect, useState } from 'react';
import {Navbar} from "./Navbar";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { getAuthToken } from '../helpers/axios_helper'
import LoginForm from './LoginForm';
import BoardsList from './BoardsList';
import TaskBoard from './TaskBoard';

function App() {
  const [isAuthenticated] = useState(getAuthToken() !== null && getAuthToken() !== "null");
  return (
    <BrowserRouter>
      <Navbar  isAuthenticated={isAuthenticated}/>
      <Routes>
      {isAuthenticated ? (
              <>
                <Route path="/boards" element={<BoardsList/>}/>
                <Route path="/boards/:boardId" component={TaskBoard} />
              </>
            ) : (
              <>
                <Route path="/" element={<LoginForm />} />
                <Route path="/boards" element={<BoardsList/>}/>
                <Route path="/test" element={<TaskBoard/>}/>
              </>
            )}
      </Routes>
    </BrowserRouter>
  );
}

export default App;
