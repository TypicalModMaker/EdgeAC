package xyz.edge.ac.packetevents.utils;

import java.util.Optional;
import com.mojang.datafixers.util.Either;

public class MojangEitherUtil
{
    public static <L, R> Either<L, R> makeLeft(final L left) {
        return (Either<L, R>)Either.left(left);
    }
    
    public static <L, R> Either<L, R> makeRight(final R right) {
        return (Either<L, R>)Either.right(right);
    }
    
    public static <L> Optional<L> getLeft(final Object either) {
        return ((Either)either).left();
    }
    
    public static <R> Optional<R> getRight(final Object either) {
        return ((Either)either).right();
    }
}
