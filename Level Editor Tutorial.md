# The Level Editor Tiles Format
The format is very easy to understand, but there are many tiles types.  
Once you read this detailed tutorial once, you won't need to come back later. It's easy to remember and master.

### The Tiles Format
The format is made out of 3 parts:
1. **[The star information (optional)](#star-information-optional)**
2. **[The tile name](#the-tile-names)**
3. **[The tile modifiers](#the-modifiers)**

The tiles format is:
```
[star](Tile-Name):(Tile-Modifier)
```

## The Modifiers
There are 2 modifier types so far - color and direction.

### The Color Modifiers
* r - red
* b - blue
* y - yellow
* g - green
* w - white
* c - cyan
* p - purple
* o - orange

### The Direction Modifiers
* u - up
* d - down
* l - left
* r - right

## The Tile Names
* w - wall tile (empty square on board)
* t - tile (just a normal blank tile)
* p - player tile, with color modifers (player starting position)
* e - end tile, with color modifers (player ending position)
* b - bucket tile, with a color modifier (change the player color)
* a - arrow tile, with a direction modifier (move the player to direction)
* r - brush tile, with a color modifier (add a color to the player)

### Star Information (Optional)
**At the moment you can only add stars to the normal blank tiles.**   
To add a star to a tile, just add "s" before the tile name.  
If you want to add a colored star, add a "s:<color>" before the tile name.

## Examples
* w - wall tile
* p:r - red player
* e:b - blue end
* b:o - orange bucket
* a:d - arrow down
* st - tile with a star
* s:gt - tile with a green star

-------
## Advanced Tiles
### Players
You can create players with multiple colors.  
You can do that by using few color modifers.   
For example:
* p:rb - player colored red and blue
* p:pcw - player colored purple, cyan and white

### Ends
You can create end tiles with multiple colors, and multiple options of colors.  
You can do that by using few color modifers separated by "+" (and) or "|" (or). You can use braces as well.  
For example:
* e:r+b - end tile colored red and blue
* e:r|b - end tile colored red or blue
* e:(r+b)|g - end tile colored [red and blue], or green
* e:r+(b|g) - end tile colored red, and [blue or green]. This tile could also by written as e:(r+b)|(r+g)
* e:w|y|c|(r+b) - end tile colored white, or yellow, or cyan, or [red and blue]
