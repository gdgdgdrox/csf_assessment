import { Restaurant } from './../models';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RestaurantService } from '../restaurant-service';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent implements OnInit{
  restaurant!: Restaurant;
  image!: string;
  constructor(private activatedRoute: ActivatedRoute, private restaurantSvc: RestaurantService){}

  ngOnInit(): void {
    const id = this.activatedRoute.snapshot.params['id'];
    console.log(`ID > ${id}`);
    this.restaurantSvc.getRestaurant(id)
        .then(response => {
          console.log(response);
          this.restaurant = response as Restaurant;
          console.log(this.restaurant);
        })
        .catch(error => console.log(error));
  }

	// TODO Task 4 and Task 5
	// For View 3

}
