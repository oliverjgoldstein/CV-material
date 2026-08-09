public class Cat extends Animal {
    @Override
    void eat(Food food) {
    food.eaten(this);
  }
}
