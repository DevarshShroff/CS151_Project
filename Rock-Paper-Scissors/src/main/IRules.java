public interface IRules {
    
    // @return 0 -> draw, 1 -> p1 wins, 2 -> p2 wins
    int decideWinner(Move p1Move, Move p2Move);

}
