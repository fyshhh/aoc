function parseinput()::Vector{Int}
    return readlines("y2020/day9/input.in") .|> s -> parse(Int, s)
end

function partone(vec::Vector{Int})::Int
    for i in 26:length(vec)
        valid = false
        for j in i-25:i-1, k in i-25:i-1
            if ((j != k) && (vec[i] == vec[j] + vec[k]))
                valid = true
            end
        end
        if !valid
            flush(stdout)
            println(vec[i])
            return i
        end
    end
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
