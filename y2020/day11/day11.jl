function parseinput()::Array{Union{Missing, Bool}, 2}
    inp = (readlines("y2020/day11/input.in") .|> collect) |> a -> hcat(a...)
    arr = similar(inp, Union{Missing, Bool})
    for i in CartesianIndices(inp)
        arr[i] = inp[i] == '.' ? missing : 0
    end
    return arr
end

function partone(arr::Array{Union{Missing, Bool}, 2})

    range = CartesianIndices((-1:1, -1:1))

    prev = similar(arr)
    curr = copy(arr)
    inds = filter(i -> !ismissing(curr[i]), CartesianIndices(arr))
    while (!isequal(prev, curr))
        prev .= curr
        for i in inds
            adj = count(skipmissing(prev[i .+ range |> a -> filter(j -> checkbounds(Bool, arr, j) && i != j, a)]))
            curr[i] = prev[i] == 0 ? adj == 0 : adj < 4
        end
    end
    flush(stdout)
    count(skipmissing(curr)) |> println
end

function parttwo(arr::Array{Union{Missing, Bool}, 2})

    range = setdiff(CartesianIndices((-1:1, -1:1)), [CartesianIndex(0, 0)])

    prev = similar(arr)
    curr = copy(arr)
    inds = filter(i -> !ismissing(curr[i]), CartesianIndices(arr))

    function cansee(start::CartesianIndex, dir::CartesianIndex)::Bool
        return checkbounds(Bool, arr, start + dir) && (ismissing(prev[start + dir]) ? cansee(start + dir, dir) : prev[start + dir])
    end
    while (!isequal(prev, curr))
        prev .= curr
        for i in inds
            adj = (range .|> r -> cansee(i, r)) |> count
            curr[i] = prev[i] == 0 ? adj == 0 : adj < 5
        end
    end
    flush(stdout)
    count(skipmissing(curr)) |> println
end

arr = parseinput()
@time partone(arr)
@time parttwo(arr)
