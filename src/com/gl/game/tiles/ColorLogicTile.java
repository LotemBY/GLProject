package com.gl.game.tiles;

import com.gl.types.ColorExp;
import com.gl.types.Direction;
import com.gl.types.LogicalExpNode;
import com.gl.types.TileColor;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public abstract class ColorLogicTile extends GameTile {

    protected static final double COLORS_TONE = 1;

    protected ColorExp colorExp;

    protected HashSet<HashSet<TileColor>> colorTextures;

    public ColorLogicTile(ColorLogicTile other){
        super(other);
        this.colorExp = other.colorExp;
        this.colorTextures = (HashSet<HashSet<TileColor>>) other.colorTextures.clone();
    }

    public ColorLogicTile(ColorExp expression){
        super();
        this.colorExp = expression;
        colorTextures = initTextureData(expression);
    }

    private static HashSet<HashSet<TileColor>> initTextureData(ColorExp exp){
        HashSet<HashSet<TileColor>> options = new HashSet<>();

        if (exp instanceof LogicalExpNode){
            LogicalExpNode logic = (LogicalExpNode) exp;
            HashSet<HashSet<TileColor>> first = initTextureData(logic.getFirst());
            HashSet<HashSet<TileColor>> second = initTextureData(logic.getSecond());
            if (logic.isAndOp()){
                for (HashSet<TileColor> colors : first){
                    HashSet<TileColor> copy = (HashSet<TileColor>) colors.clone();

                    for (HashSet<TileColor> colorsSecond : second){
                        copy.addAll(colorsSecond);
                        options.add(copy);
                        copy = (HashSet<TileColor>) colors.clone();
                    }
                }
            } else {
                options.addAll(first);
                options.addAll(second);
            }
        } else {
            HashSet<TileColor> set = new HashSet<>();
            set.add((TileColor) exp);

            options.add(set);
        }

        return options;
    }

    public boolean canPassFrom(Direction from, List<TileColor> playerColors){
        return super.canPassFrom(from, playerColors) &&
                colorTextures.contains(new HashSet<>(playerColors));

    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof ColorLogicTile)) return false;
        if (!super.equals(o)) return false;
        ColorLogicTile that = (ColorLogicTile) o;
        return Objects.equals(colorTextures, that.colorTextures);
    }

    @Override
    public int hashCode(){
        return Objects.hash(super.hashCode(), colorTextures);
    }
}
