function parseinput()::Vector{String}
    return readlines("y2020/day12/input.in")
end

function partone(vec::Vector{String})

    movarr = Dict{String, CartesianIndex}("N" => CartesianIndex(0, 0, 1), "E" => CartesianIndex(0, 1, 0), "S" => CartesianIndex(0, 0, -1),
        "W" => CartesianIndex(0, -1, 0), "R" => CartesianIndex(1, 0, 0), "L" => CartesianIndex(-1, 0, 0))
    dirarr = ["N", "E", "S", "W"]

    curr = CartesianIndex(2, 0, 0)

    for str in vec
        dir = String(SubString(str, 1:1))
        amt = parse(Int, SubString(str, 2))
        if dir == "F"
            curr += amt * movarr[dirarr[mod1(curr[1], 4)]]
        elseif dir == "R" || dir == "L"
            curr += div(amt, 90) * movarr[dir]
        else
            curr += amt * movarr[dir]
        end
    end

    abs(curr[2]) + abs(curr[3]) |> println

end

function parttwo(vec::Vector{String})

    movarr = Dict{String, CartesianIndex}("N" => CartesianIndex(0, 1), "E" => CartesianIndex(1, 0),
        "S" => CartesianIndex(0, -1), "W" => CartesianIndex(-1, 0))
    dirarr = [i -> CartesianIndex(i[2], -i[1]), i -> CartesianIndex(-i[1], -i[2]), i -> CartesianIndex(-i[2], i[1]), identity]

    ship = CartesianIndex(0, 0)
    wayp = CartesianIndex(10, 1)

    for str in vec
        dir = String(SubString(str, 1:1))
        amt = parse(Int, SubString(str, 2))
        if dir == "F"
            diff = wayp - ship
            ship += amt * diff
            wayp += amt * diff
        elseif dir == "R" || dir == "L"
            diff = wayp - ship
            wayp = ship + dirarr[mod1((dir == "L" ? -1 : 1) * div(amt, 90), 4)](diff)
        else
            wayp += amt * movarr[dir]
        end
    end

    abs(ship[1]) + abs(ship[2]) |> println

end

input = parseinput()
partone(input)
parttwo(input)
