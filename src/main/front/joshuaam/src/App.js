import React, { lazy, Suspense, useEffect, useState } from "react";
import "./App.css";
import axios from "axios";
import NavbarComponents from "./components/NavbarComponent";
import SidebarMenu from "./components/SidebarMenu";
import { Navbar, Nav, Container } from "react-bootstrap";
import { Routes, Route, Link, useNavigate, Outlet } from "react-router-dom";
import AttendanceCheck from "./components/attendance/AttendanceCheck.js";
import Home from "./Home.js";

const Attendances = lazy(() => import('./components/attendance/Attendances.js'));
const Members = lazy(() => import('./components/member/Members.js'));
const Groups = lazy(() => import('./components/group/Groups.js'));

function App() {

  let navigate = useNavigate();

  return (
    <div>
      {/* <NavbarComponents/> */}
      {/* <SidebarMenu/> */}

      <Navbar bg="light" data-bs-theme="light">
        <Container>
          <Navbar.Brand href="/">Joshua AM</Navbar.Brand>
          <Nav className="me-auto text-nowrap">
            {/* <Nav.Link href="/">홈</Nav.Link> */}
            <Nav.Link onClick={() => { navigate("/"); }}>홈</Nav.Link>
            <Nav.Link onClick={() => { navigate("/attendances"); }}>출석</Nav.Link>
            <Nav.Link onClick={() => { navigate("/attendanceCheck"); }}>출석체크</Nav.Link>
            <Nav.Link onClick={() => { navigate("/members"); }}>회원</Nav.Link>
            <Nav.Link onClick={() => { navigate("/groups"); }}>그룹</Nav.Link>
          </Nav>
          <Nav className="me-auto">
            {/* { result.isLoading ? '로딩중': result.data.name } */}
            {/* {result.isLoading && "로딩중"}
            {result.isError && "에러남"}
            {result.data && result.data.name} */}
          </Nav>
        </Container>
      </Navbar>
      
      <div className="container">
      <Suspense fallback={<div>로딩중임</div>}>
        <Routes>
          <Route path="/" element={ <Home/> }/>
          <Route path="/attendances/*" element={ <Attendances/> }/>
          <Route path="/attendanceCheck" element={ <AttendanceCheck/> }/>
          <Route path="/members/*" element={ <Members/> }/>
          <Route path="/groups/*" element={ <Groups/> }/>
        </Routes>
      </Suspense>
      </div>
    </div>
  );
}

export default App;
