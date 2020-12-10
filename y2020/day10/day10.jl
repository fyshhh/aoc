function parseinput()::Vector{Int}
    return readlines("y2020/day10/input.in") .|> s -> parse(Int, s)
end

using LinearAlgebra
function partone(vec::Vector{Int})
    Bidiagonal(fill(-1, length(vec)), fill(1, length(vec) - 1), :U) * sort(vec) |> v -> ((count(==(1), v) + 1) * (count(==(3), v) + 1)) |> println
end

function parttwo(vec::Vector{Int})
    dict = Dict{Int, Int}(max(vec...) => 1)
    function memoread(val::Int)
        if haskey(dict, val)
            return dict[val]
        elseif (val in vec || val == 0)
            dict[val] = memoread(val + 1) + memoread(val + 2) + memoread(val + 3)
            return dict[val]
        else
            return 0
        end
    end
    memoread(0) |> println
end

arr = parseinput()
partone(arr)
parttwo(arr)
