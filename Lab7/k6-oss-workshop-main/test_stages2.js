import http from "k6/http";

const BASE_URL = __ENV.BASE_URL || "http://localhost:3333";

export const options = { 
  stages: [ 
    // ramp up from 0 to 20 VUs over the next 30s seconds 
    { duration: '30s', target: 20 }, 
    // run 20 VUs over the next 30 seconds 
    { duration: '30s', target: 20 }, 
    // ramp down from 20 to 0 VUs over the next 30 seconds 
    { duration: '30s', target: 0 }, 
  ], 
  
  thresholds: { 
    Errors: ['count<100'],
    http_req_duration: ['p(95)<200'],
  }, 
 };

export default function () {
  let restrictions = {
    maxCaloriesPerSlice: 500,
    mustBeVegetarian: false,
    excludedIngredients: ["pepperoni"],
    excludedTools: ["knife"],
    maxNumberOfToppings: 6,
    minNumberOfToppings: 2,
  };
  let res = http.post(`${BASE_URL}/api/pizza`, JSON.stringify(restrictions), {
    headers: {
      "Content-Type": "application/json",
      "X-User-ID": 23423,
      "Authorization": "token abcdef0123456789",
    },
  });
  console.log(`${res.json().pizza.name} (${res.json().pizza.ingredients.length} ingredients)`);
}