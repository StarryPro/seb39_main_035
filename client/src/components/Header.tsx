import { Link } from 'react-router-dom';
import styled from 'styled-components';
import { useSelector } from 'react-redux';
import { RootState } from '../stores/store';
import logo from '../assets/1.png';
import { TbBooks, TbCalendarStats } from 'react-icons/tb';
import { BiBookHeart } from 'react-icons/bi';
import { BsPersonCircle } from 'react-icons/bs';
import { GiArchiveResearch } from 'react-icons/gi';

const HeaderWrapper = styled.header`
  background-color: #f9f9f9;
  height: 60px;
  padding: 0 12px;
  position: sticky;
  top: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 4px 6px rgb(32 33 36 / 10%);
`;

const Logo = styled.div`
  height: 100%;
  margin: auto;
  cursor: pointer;
  img {
    height: 60px;
  }
`;

const Menu = styled.ul`
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  width: 60%;
  height: 100%;

  svg {
    cursor: pointer;
    padding: 10px auto;
    font-size: 28px;
    color: #b3dbd8;
    margin: 0 10px;
  }
`;

const Header = () => {
  const { isLoggedIn } = useSelector((state: RootState) => state.user);

  return (
    <>
      <HeaderWrapper>
        {!isLoggedIn && (
          <Link to='/'>
            <Logo>
              <img src={logo} alt='logo_icon' />
            </Logo>
          </Link>
        )}
        {isLoggedIn && (
          <>
            <Link to='/books/library'>
              <Logo>
                <img src={logo} alt='logo_icon' />
              </Logo>
            </Link>
            <Menu>
              <Link to='/books/library'>
                <TbBooks />
              </Link>
              <BiBookHeart />
              <GiArchiveResearch />
              <TbCalendarStats />
              <Link to='/mypage'>
                <BsPersonCircle />
              </Link>
            </Menu>
          </>
        )}
      </HeaderWrapper>
    </>
  );
};

export default Header;
