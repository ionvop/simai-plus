# simai-plus
A transpiler for converting my own interpretation of the simai notation to the standard notation used in maidata. Mostly for personal use.

## Differences

### Length dividers

Instead of writing `{n}` to define the length divider, it is now defined by its nesting level within the curly brackets.

```
{
    {
        1,2,3,4,
        
        {
            1,2,3,4,
        }
    }
    
    1,2,3,4,
}
```

It behaves the same as

```
{2}
1,2,3,4,

{4}
1,2,3,4,

{1}
1,2,3,4,
```

To which a lot of people may think that it just makes it more complicating, but I personally prefer the top one for some reason.

### Comments

It is now possible to write comments within the fumen which will be ignored when transpiled.

```
{
    {
        // Hello, world!
        1,2,3,4,
        
        {
            1,2,3,4,
        }
    }
    
    1,2,3,4,
}
```

### Slide waiting time

It is now possible to use note lengths to determine the waiting time of a slide instead of just one beat of the bpm by using 3 numbers signs `###` as the separator between the waiting time and the tracing length. The transpiler will calculate the milliseconds of the note lengths by the current bpm which will be used in the transpiled fumen.

```
(170)

{
    1-5[2:1###16:1],
}
```

It behaves the same as

```
(170)

{1}
1-5[0.706##0.088],
```
