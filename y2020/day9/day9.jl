function parseinput()::Vector{Int}
    return readlines("y2020/day9/input.in") .|> s -> parse(Int, s)
end

function partone(vec::Vector{Int})::Int
    for i in 26:length(vec)
        if !([(j, k) for j=i-25:i-1 for k=j:i-1] |>
            a -> mapreduce(t -> vec[i] == vec[t[1]] + vec[t[2]], |, a))
            flush(stdout)
            println(vec[i])
            return i
        end
    end
    return 1
end

function parttwo(vec::Vector{Int}, ind::Int)::Nothing
    sum = 0
    min = 1
    max = 0
    while (true)
        if (sum == vec[ind])
            return extrema(vec[min:max]) |> t -> t[1] + t[2] |> println
        elseif (sum < vec[ind])
            max += 1
            sum += vec[max]
        elseif (sum > vec[ind])
            sum -= vec[min]
            min += 1
        end
    end
end

function main()
    arr = parseinput()
    ind = partone(arr)
    parttwo(arr, ind)
end

main()
