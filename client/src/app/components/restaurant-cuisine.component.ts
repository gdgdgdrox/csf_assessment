import { RestaurantNameAndID } from './../models';
import { RestaurantService } from './../restaurant-service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-restaurant-cuisine',
  templateUrl: './restaurant-cuisine.component.html',
  styleUrls: ['./restaurant-cuisine.component.css']
})
export class RestaurantCuisineComponent implements OnInit{
  cuisine!: string;
  restaurantNameAndIDArray!: RestaurantNameAndID[];

	constructor(private activatedRoute: ActivatedRoute, private restaurantSvc: RestaurantService){}

  ngOnInit(): void {
    this.cuisine = this.activatedRoute.snapshot.params['cuisine'];
    console.log(`cuisine requested> ${this.cuisine}`);
    this.restaurantSvc.getRestaurantsByCuisine(this.cuisine).then((response:any) => {
      console.log((response));
      this.restaurantNameAndIDArray = response.restaurants as RestaurantNameAndID[];

    })
    .catch(error => console.log(error))
  }


	// TODO Task 3
	// For View 2

}
