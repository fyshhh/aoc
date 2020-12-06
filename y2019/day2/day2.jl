struct Intcode
    code::Dict{Int, Int}
    function Intcode()
        dict = Dict{Int, Int}()
        array = split(read("y2019/day2/input.txt", String), ",") .|>
            s -> parse(Int, s)
        for (x, y) in zip(array, 0:typemax(Int))
            setindex!(dict, x, y)
        end
        return new(dict)
    end
end

function iterateonce(ic::Intcode, index::Int)::Int
    opcode = get(ic.code, index, 0)
    arg1 = get(ic.code, get(ic.code, index + 1, 0), 0)
    arg2 = get(ic.code, get(ic.code, index + 2, 0), 0)
    output = get(ic.code, index + 3, 0)
    if opcode == 1
        setindex!(ic.code, arg1 + arg2, output)
        return index + 4
    elseif opcode == 2
        setindex!(ic.code, arg1 * arg2, output)
        return index + 4
    elseif opcode == 99
        return -1
    else
        error("read error at index $(index)")
    end
end

function partone()
    computer = Intcode()
    setindex!(computer.code, 12, 1)
    setindex!(computer.code, 2, 2)
    index = 0
    while index != -1
        index = iterateonce(computer, index)
    end
    get(computer.code, 0, -1) |> println
end

function iteratewithinputs(i, j)::Int
    computer = Intcode()
    setindex!(computer.code, i, 1)
    setindex!(computer.code, j, 2)
    index = 0
    while index != -1
        index = iterateonce(computer, index)
    end
    return get(computer.code, 0, -1)
end

function parttwo()
    computer = Intcode()
    for i=0:99, j=0:99
        if iteratewithinputs(i, j) == 19690720
            flush(stdout)
            (100 * i + j) |> println
            break
        end
    end
end

function main()
    partone()
    parttwo()
end

main()
