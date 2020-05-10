# CursedRenderThing 

A port of an old version of [GLUtils](https://www.minecraftforum.net/forums/mapping-and-modding-java-edition/minecraft-mods/1294081-any-version-of-minecraft-minecraft-glutils-obj) to [minecraft-cursed-legacy's port of fabric to beta 1.7.3](https://github.com/minecraft-cursed-legacy)
It also contains an example that renders a ro

## A word about file layout

All mtl and image files must be in the same folder as the obj file. You must remove all paths from the file. Eg:
```map_Kd \MMSEV_Color_v2s.png``` or ```map_Kd ./MMSEV_Color_v2s.png``` becomes: ```map_Kd MMSEV_Color_v2s.png```.
I have no clue why this is necessary but it is.

## PR's

If you submit pull request that make this more like a proper library they will likely be accepted.