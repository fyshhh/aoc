function parseinput()::Vector{CartesianIndex{2}}
    vect = Vector{CartesianIndex}(undef, 0)
    for i in 1:countlines("y2020/day17/input.in")
        line = readlines("y2020/day17/input.in")[i]
        for j in 1:length(line)
            if getindex(line, j) == '#'
                push!(vect, CartesianIndex(i - 1, j - 1))
            end
        end
    end
    return vect
end

function partone(inp::Vector{CartesianIndex{2}})

    nbrs = Dict{CartesianIndex, Set{CartesianIndex}}()

    function retrieve(ind::CartesianIndex{3})
        return get!(nbrs, ind, ind .+ CartesianIndices((-1:1, -1:1, -1:1)) |> v -> delete!(Set(v), ind))
    end

    state = Dict{CartesianIndex, Pair{Bool, Bool}}()

    function activate(ind::CartesianIndex)::Bool
        val = sum(i -> get(state, i, 0 => 0).first, retrieve(ind))
        return val == 3 || (val == 2 && get(state, ind, 0 => 0).first)
    end

    inp .|> ci -> state[CartesianIndex(ci[1], ci[2], 0)] = 1 => 0

    for _ in 1:6
        inds = union((keys(state) .|> retrieve)...)
        for ind in inds
            state[ind] = get(state, ind, 0 => 0).first => activate(ind)
        end
        map!(p -> p.second => 0, values(state))
    end

    flush(stdout)
    count(values(state) .|> p -> p.first) |> println

end

function parttwo(inp::Vector{CartesianIndex{2}})

    nbrs = Dict{CartesianIndex, Set{CartesianIndex}}()

    function retrieve(ind::CartesianIndex{4})
        return get!(nbrs, ind, ind .+ CartesianIndices((-1:1, -1:1, -1:1, -1:1)) |> v -> delete!(Set(v), ind))
    end

    state = Dict{CartesianIndex, Pair{Bool, Bool}}()

    function activate(ind::CartesianIndex)::Bool
        val = sum(i -> get(state, i, 0 => 0).first, retrieve(ind))
        return val == 3 || (val == 2 && get(state, ind, 0 => 0).first)
    end

    inp .|> ci -> state[CartesianIndex(ci[1], ci[2], 0, 0)] = 1 => 0

    for _ in 1:6
        inds = union((keys(state) .|> retrieve)...)
        for ind in inds
            state[ind] = get(state, ind, 0 => 0).first => activate(ind)
        end
        map!(p -> p.second => 0, values(state))
    end

    flush(stdout)
    count(values(state) .|> p -> p.first) |> println

end

inp = parseinput()
partone(inp)
# 11s runtime sadge - too many allocations, likely due to assignments in nbrs and state
parttwo(inp)
