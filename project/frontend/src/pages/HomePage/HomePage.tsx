import React, { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import CampCard from "../../components/CampCard";
import { Pagination, PaginationItem, PaginationLink, Row, Col } from 'reactstrap';
import PagePagintion from "../../components/PagePagingtion";
// import ReadMoreReact from 'read-more-react';
import ImageCarousel from "../../components/ImageCarousel/ImageCarousel";
import Loading from "../../components/Loading";
import * as API from "../../service";
import * as Model from "../../type";
import 'react-multi-carousel/lib/styles.css';
import {getLink} from "../../utils/Utils"
import "./HomePage.css";
import { Type } from "typescript";
interface Props { }


const HomePage = (props: Props) => {
  const [isLoading, setLoading] = useState(false);
  const [listCamp, setListCamp] = useState([] as any);

  useEffect(() => {
    setLoading(true);
    fetchCamps();
  }, [])

  const fetchCamps = () => {
    API.getListCamp()
      .then(response => {
        const listCampData = response.data;
        console.log(listCampData)
        if (listCampData) {
          setListCamp(listCampData.data);
        }
        setLoading(false);
      })
      .catch(error => {
        setLoading(false);
      })
  }
  return (
    <div className="container home">
      <Row>
        <ImageCarousel data = {listCamp}/>
      </Row>

      <Row>
        <Col md={8} className="my-4">
          <h1>Pinecrest</h1>

          {/* <ReadMoreReact 
          text={"Pinecrest features a large campground by Pinecrest Lake, just 30 miles east of Sonora, at an elevation of 5600 feet. The area includes a day-use beach and a marina, a small shopping center and recreation cabins. The campground caters to all ages and is within walking distance of the lake, an amphitheater, visitor center, swimming beach and spectacular hiking trails. Pets are welcome, but must be compliant with Tuolumne County leash laws."} /> */}
        </Col>
      </Row>

      <Row className="mt-5">
        <Col md={6}>
          <div className="listcamp">
            {isLoading && <Loading />}
            {listCamp.map((camp: Model.Camp) => {
              return (
                <CampCard camp={camp} />
              )
            })}
          </div>
        </Col>
        <Col md={5}>
          <img
            className="w-100"
            src="https://cdn.recreation.gov/public/2019/02/21/19/34/65350_e7c97c84-39f2-425b-9714-4566d2fbaf8f_700.jpg"
            alt=""
          />
        </Col>
      </Row>

      <Row className="mt-2">
        <Col md={6}>
          {/* <div className="d-flex justify-content-between align-items-center">
            <span>1 - 10 results of 196</span>
            <PagePagintion />
          </div> */}

        </Col>
      </Row>


    </div>
  );
};

export default HomePage;
