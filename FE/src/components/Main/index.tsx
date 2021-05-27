import { useMainDispatch, useMainState } from '../../contexts/MainContext';
import { Text, TextContentInfo, TextFooter, TextTopBackground } from '../../util/reference';

import Background from '../Common/Background';
import BackgroundFluid from '../Common/BackgroundFluid';

import TopBackground from './TopBackground';
import Nearby from './Nearby';
import RoomType from './RoomType';
import ContentInfo from './ContentInfo';
import Footer from './Footer';

const Main = () => {
  const { nearby, roomType } = Text;
  const { contentInfo } = TextContentInfo;
  const { footerItems } = TextFooter;
  const { headerTexts, searchBarTexts } = TextTopBackground;

  const { searchBarClickedIdx } = useMainState();
  const mainDispatch = useMainDispatch();
  const handleBackgroundFluidClick = () => {
    if (searchBarClickedIdx < 0) return;
    mainDispatch({ type: 'CHANGE_SEARCHBAR_CLICKED_IDX', payload: -1 });
  };

  return (
    <BackgroundFluid onClick={handleBackgroundFluidClick}>

      <TopBackground
        headerTexts={headerTexts}
        searchBarTexts={searchBarTexts}
      />
      <Nearby nearbyItems={nearby} />
      <RoomType roomTypeItems={roomType} />

      <Background backgroundColor={'gray6'}>
        <ContentInfo contentInfoItems={contentInfo} />
        <Footer footerItems={footerItems} />
      </Background>

    </BackgroundFluid>
  );
};

export default Main;