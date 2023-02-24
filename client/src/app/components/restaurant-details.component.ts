import { Restaurant } from './../models';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RestaurantService } from '../restaurant-service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-restaurant-details',
  templateUrl: './restaurant-details.component.html',
  styleUrls: ['./restaurant-details.component.css']
})
export class RestaurantDetailsComponent implements OnInit{
  restaurantId!: string;
  restaurant!: Restaurant;
  form!: FormGroup;
  constructor(private activatedRoute: ActivatedRoute, 
              private restaurantSvc: RestaurantService, 
              private fb: FormBuilder,
              private router: Router
              ){}

  ngOnInit(): void {
    this.restaurantId = this.activatedRoute.snapshot.params['id'];
    this.restaurantSvc.getRestaurant(this.restaurantId)
        .then(response => {
          this.restaurant = response as Restaurant;
        })
        .catch(error => console.log(error));
    
      this.form = this.createForm();
  }

  postComment(){
    const comment = {
      name: this.form.value.name,
      rating: this.form.value.rating,
      restaurantId: this.restaurantId,
      text: this.form.value.text
    }
    
    //post comment
    this.restaurantSvc.postComment(comment)
          .then(response =>{
            if (response.message === 'Comment posted'){
                this.router.navigate(['/']);
            }
            else{
              alert("something went wrong");
            }
          })
          .catch(error => {
            console.log(error);
            alert(error.error);
          })


  }

  createForm(){
    return this.fb.group({
      name: this.fb.control<string>('', Validators.minLength(4)),
      rating: this.fb.control<number>(1,[Validators.minLength(1), Validators.maxLength(5)]),
      text: this.fb.control('', Validators.required)
    }
    )
  }

	


}
