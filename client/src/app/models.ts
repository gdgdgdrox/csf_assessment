// Do not change these interfaces
export interface Restaurant {
	restaurantId: string
	//changed "namd" to "name" as i assume its a typo
	name: string
	//updated typo
	cuisine: string
	address: string
	coordinates: number[]
}

export interface Comment {
	name: string
	rating: number
	restaurantId: string
	text: string
}

export interface RestaurantNameAndID{
	name: string,
	id: string
}

// export interface RestaurantWithImage{
// 	restaurantId: string,
// 	name: string,
// 	address: string,
// 	cuisine: string,
// 	image: string
// }