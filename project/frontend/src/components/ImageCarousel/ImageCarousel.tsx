import React from "react"
import * as Model from "../../type";
import Carousel from 'react-multi-carousel';
import {getLink} from "../../utils/Utils";


interface ICarouselProps {
    data: Array<Model.Camp>
}

const responsive = {
    superLargeDesktop: {
      // the naming can be any, depends on you.
      breakpoint: { max: 4000, min: 3000 },
      items: 5
    },
    desktop: {
      breakpoint: { max: 3000, min: 1024 },
      items: 3
    },
    tablet: {
      breakpoint: { max: 1024, min: 464 },
      items: 2
    },
    mobile: {
      breakpoint: { max: 464, min: 0 },
      items: 1
    }
  };

const ImageCarousel = ({data}: ICarouselProps) => {

    return (
        <Carousel responsive={responsive}>
           
            {data.map(camp => {
                const link = getLink(camp.mainImgSrc);
                console.log(link)
                return (
                    <div className="mx-3"
                        key = {`carousel_${camp._id}`}
                    >
                        <img
                            className="w-100"
                            src= {link} 
                            alt=""
                        />
                    </div>
                )
            })}
        </Carousel>
    )
}


export default ImageCarousel