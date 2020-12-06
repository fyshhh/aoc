function parsegroup()::Array{<:AbstractString}
    return split(read("y2020/day6/input.in", String), "\n\n")
end

function partone(array::Array{<:AbstractString})
    (array .|> s -> replace(s, "\n" => "") |> Set) |>
        a -> sum(length, a) |> println
end

function parttwo(array::Array{<:AbstractString})
    (array .|> s -> split(s, "\n") .|> Set |> s -> intersect(s...)) |>
        a -> sum(length, a) |> println
end

function main()
    array = parsegroup()
    flush(stdout)       # more juno problems
    partone(array)
    parttwo(array)
end

main()
