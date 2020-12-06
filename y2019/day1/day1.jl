function getfuel(in::Int)::Int
    return floor(Int, in / 3) - 2;
end

function gettotalfuel(in::Int)::Int
    fuel = getfuel(in)
    return fuel < 0 ? 0 : fuel + gettotalfuel(fuel)
end

function generate()::Array{Int}
    return split(read("y2019/day1/input.txt", String), "\n") .|>
        s -> parse(Int, s)
end

function partone(array::Array{Int})
    (array .|> getfuel) |> sum |> println
end

function parttwo(array::Array{Int})
    (array .|> gettotalfuel) |> sum |> println
end

function main()
    array = generate()
    partone(array)
    parttwo(array)
end

main()
